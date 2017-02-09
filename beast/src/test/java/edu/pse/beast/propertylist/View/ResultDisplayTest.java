package edu.pse.beast.propertylist.View;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.pse.beast.datatypes.FailureExample;
import edu.pse.beast.datatypes.electiondescription.ElectionType;
import edu.pse.beast.propertychecker.CBMC_Result_Wrapper_long;
import edu.pse.beast.propertychecker.CBMC_Result_Wrapper_singleArray;

public class ResultDisplayTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
	}

	public static void main(String[] args) {
		
		ResultPresenterWindow win = new ResultPresenterWindow();
		win.setVisible(true);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		ElectionType et = ElectionType.SINGLECHOICE;
		List<Long[]> vote = new ArrayList<Long[]>();
		List<Long> elected = new ArrayList<Long>();
		vote.add(new Long[]{3l,2l,1l});
		vote.add(new Long[]{2l,2l,1l});
		elected.add(3l);
		elected.add(2l);
		
		ArrayList<CBMC_Result_Wrapper_singleArray> votes = new ArrayList<CBMC_Result_Wrapper_singleArray>();
		ArrayList<CBMC_Result_Wrapper_long> elect = new ArrayList<CBMC_Result_Wrapper_long>();
		
		votes.add(new CBMC_Result_Wrapper_singleArray(0, "wildelection"));
		votes.get(0).addTo(0, 3l);
		votes.get(0).addTo(1, 2l);
		votes.get(0).addTo(2, 1l);
		votes.add(new CBMC_Result_Wrapper_singleArray(1, "wilderelection"));
		votes.get(1).addTo(0, 3l);
		votes.get(1).addTo(1, 3l);
		votes.get(1).addTo(2, 3l);
		
		elect.add(new CBMC_Result_Wrapper_long(0, "wildcandidate"));
		elect.get(0).setValue(2l);
		elect.add(new CBMC_Result_Wrapper_long(1, "wildercandidate"));
		elect.get(1).setValue(3l);
		
		
		FailureExample fail = new FailureExample(et, votes, null, elect, null, 4, 1, 3);
	
		win.presentFailureExample(fail);
		
		
	}
}
