import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface GithubClient {

    @GET("users/{user}/repos")
    List<Repository> listRepos(@Path("user") String user);

}
