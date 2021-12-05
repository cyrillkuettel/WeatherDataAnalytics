package ch.hslu.swde.wda.persister;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ch.hslu.swde.wda.domain.City;

public class PersistCityTest {

	@Test
	void testSelectWeatherByDataAndCity() {

		City bern = new City(3000,"Bern");
		City zurich = new City(8000,"Zurich");
		City basel = new City(4000,"Basel");
		
		List<City> cities = new ArrayList<City>();
		cities.add(bern);
		cities.add(basel);
		cities.add(zurich);
		
//		PersistCity.insertCities(cities);

//		assertThat(cities.size()).isNotZero();

	}

}
