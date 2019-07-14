import org.junit.jupiter.api.Test;

public class GithubTest {

    @Test
    void getReposTest() {
        GithubClientImpl.createGithubClient()
                .listRepos("ramich2077")
                .forEach(repo -> {
                    System.out.println(repo.getName());
                });
    }
}
