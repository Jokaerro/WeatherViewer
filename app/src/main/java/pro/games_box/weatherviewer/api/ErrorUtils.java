package pro.games_box.weatherviewer.api;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by TESLA on 06.04.2017.
 */

public class ErrorUtils {
    public static ApiError parseError(Response<?> response) {
        Converter<ResponseBody, ApiError> converter = Api.getRetrofit().responseBodyConverter(ApiError.class, new Annotation[0]);
        ApiError error = null;
        try {
            error = converter.convert(response.errorBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error;
    }
}
