package datafetch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.CompletableFuture;

public class DataProviderService {

    private String countryName;

    public CovidDataModel getData(String countryName) {
        this.countryName = countryName;

        CovidApi covidApi = createApi();
        CountryData countryData = getCountryData(covidApi);
        GlobalData  globalData  = getGlobalData (covidApi);

        return new CovidDataModel(globalData, countryData);
    }

    private CovidApi createApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://coronavirus-19-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(CovidApi.class);
    }

    private CountryData getCountryData(CovidApi covidApi) {
        CompletableFuture<CountryData> callback1 = new CompletableFuture<>();
        covidApi.getCountryData(countryName).enqueue(new Callback<CountryData>() {

            @Override
            public void onResponse(Call<CountryData> call, Response<CountryData> response) {
                callback1.complete(response.body());
            }

            @Override
            public void onFailure(Call<CountryData> call, Throwable t) {
                callback1.completeExceptionally(t);
            }
        });

        return callback1.join();
    }

    private GlobalData getGlobalData(CovidApi covidApi) {
        CompletableFuture<GlobalData> callback2 = new CompletableFuture<>();
        covidApi.getGlobalData().enqueue(new Callback<GlobalData>() {
            @Override
            public void onResponse(Call<GlobalData> call, Response<GlobalData> response) {
                callback2.complete(response.body());
            }

            @Override
            public void onFailure(Call<GlobalData> call, Throwable t) {
                callback2.completeExceptionally(t);
            }
        });

        return callback2.join();
    }
}
