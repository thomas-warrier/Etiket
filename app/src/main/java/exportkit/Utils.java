package exportkit;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import com.pixplicity.sharp.Sharp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.github.barteksc.pdfviewer.PDFView;



public class Utils {

    private static OkHttpClient httpClient;

    // this method is used to fetch svg and load it into
    // target imageview.
    public static void fetchSvg(Context context, String url,
                                final ImageView target)
    {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .cache(new Cache(
                            context.getCacheDir(),
                            5 * 1024 * 1014))
                    .build();
        }

        // here we are making HTTP call to fetch data from
        // URL.
        Request request
                = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e)
            {
                // we are adding a default image if we gets
                // any error.
                //target.setImageResource(R.drawable.gfgimage);
            }

            @Override
            public void onResponse(Call call,
                                   Response response)
                    throws IOException
            {
                // sharp is a library which will load stream
                // which we generated from url in our target
                // imageview.
                InputStream stream
                        = response.body().byteStream();
                Sharp.loadInputStream(stream).into(target);
                stream.close();
            }
        });
    }


    public static void loadPdfFromUrl(String url,final PDFView pdfView){
        new RetrivePDFfromUrl(pdfView).execute(url);
    }

    // create an async task class for loading pdf file from URL.
    static class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        PDFView pdfView;
        public RetrivePDFfromUrl(PDFView pdfView){
            this.pdfView=pdfView;
        }
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();
        }
    }
}

