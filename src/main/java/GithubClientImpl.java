import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class GithubClientImpl {

    public static GithubClient createGithubClient() {
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new DebugLoggingInterceptor())
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(new DefaultCallAdapterFactory<>())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .baseUrl("https://api.github.com")
                .build();

        return retrofit.create(GithubClient.class);
    }
}
