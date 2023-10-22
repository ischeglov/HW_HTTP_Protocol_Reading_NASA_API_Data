import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class Launcher {

    public static final String url = "https://api.nasa.gov/planetary/apod?api_key=ВАШ_КЛЮЧ";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        CloseableHttpResponse response = httpClient.execute(new HttpGet(url));

        Nasa nasa = mapper.readValue(response.getEntity().getContent(), Nasa.class);

        CloseableHttpResponse image = httpClient.execute(new HttpGet(nasa.getUrl()));
        String[] nasaArray = nasa.getUrl().split("/");
        String fileName = nasaArray[nasaArray.length - 1];

        HttpEntity entity = image.getEntity();
        if (entity != null) {
            FileOutputStream fos = new FileOutputStream(fileName);
            entity.writeTo(fos);
            fos.close();
        }

        httpClient.close();
        response.getEntity().getContent().close();
    }
}
