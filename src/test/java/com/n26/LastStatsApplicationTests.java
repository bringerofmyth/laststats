package com.n26;

import com.n26.model.BucketLinkedList;
import com.n26.model.InputData;
import com.n26.model.OutputData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LastStatsApplicationTests {

	@Autowired
	private BucketLinkedList buffer;
	private long threshold = 0L;

	@Before
	public void postTransactions(){

		threshold = (System.currentTimeMillis() / 1000) - 60;
		for (long i =0 ; i<=100; i++){
			InputData data = new InputData();
			data.setAmount((double)i);
			data.setTimestamp(System.currentTimeMillis()-1000*i);
			buffer.insertData(threshold, data);

		}
	}
	@Test
	public void getStatistics() {
		OutputData output = buffer.calculateStats(threshold);
		assertThat(output.getAvg(),is(30D));
		assertThat(output.getSum(), is(1830D));
		assertThat(output.getMax(), is(60D));
		assertThat(output.getMin(), is(0D));
		assertThat(output.getCount(), is(61L));

	}

}
