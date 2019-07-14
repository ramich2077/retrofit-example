import okhttp3.*;
import okio.Buffer;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DebugLoggingInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(DebugLoggingInterceptor.class);

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final StringBuilder logInfo = new StringBuilder(SystemUtils.LINE_SEPARATOR);
        final Request request = chain.request();
        final String requestUrl = request.url().toString();
        final String body = "Body: ";

        logInfo.append("Request: ")
                .append(request.method())
                .append(' ')
                .append(requestUrl)
                .append(SystemUtils.LINE_SEPARATOR);
        if (request.headers().size() > 0) {
            logInfo.append(request.headers().toString());
        }

        final RequestBody requestBody = request.body();
        if (Objects.nonNull(requestBody) && requestBody.contentLength() > 0) {
            logInfo.append(body)
                    .append(readRequestBody(requestBody))
                    .append(SystemUtils.LINE_SEPARATOR);
        }

        final Map<String, String> paramsMap = new HashMap<>();
        request.url().queryParameterNames().forEach(param -> paramsMap.put(param, request.url().queryParameter(param)));
        logInfo.append("Parameters: ")
                .append(paramsMap.toString())
                .append(SystemUtils.LINE_SEPARATOR);

        final Response response = chain.proceed(request);

        final Response.Builder responseBuilder = response.newBuilder();

        final ResponseBody responseBody = response.body();

        logInfo.append("Response code: ")
                .append(response.code())
                .append(SystemUtils.LINE_SEPARATOR);
        if (Objects.nonNull(responseBody)) {
            final byte[] bytes = responseBody.bytes();
            logInfo.append(body)
                    .append(new String(bytes, StandardCharsets.UTF_8));
            responseBuilder.body(ResponseBody.create(responseBody.contentType(), bytes));
        }

        log.info(logInfo.toString());

        return responseBuilder.build();
    }

    private static String readRequestBody(final RequestBody requestBody) throws IOException {
        final Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        return buffer.readString(StandardCharsets.UTF_8);
    }
}
