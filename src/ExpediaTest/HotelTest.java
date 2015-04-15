package ExpediaTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Expedia.Flight;
import Expedia.Hotel;
import Expedia.IDatabase;

public class HotelTest
{
	private Hotel targetHotel;
	private int NightsToRentHotel = 5;
	
	@Before
	public void TestInitialize()
	{
		targetHotel = new Hotel(NightsToRentHotel);
	}
	
	@Test
	public void TestThatHotelHasCorrectBasePriceForOneDayStay()
	{
		Hotel target = new Hotel(1);
		Assert.assertEquals(45, target.getBasePrice(), 0.01);
	}
	
	@Test
	public void TestThatHotelHasCorrectBasePriceForTwoDayStay()
	{
		Hotel target = new Hotel(2);
		Assert.assertEquals(90, target.getBasePrice(), 0.01);
	}
	
	@Test
	public void TestThatHotelHasCorrectBasePriceForTenDaysStay()
	{
		Hotel target = new Hotel(10);
		Assert.assertEquals(450, target.getBasePrice(), 0.01);
	}
	
	@Test(expected=RuntimeException.class)
	public void TestThatHotelThrowsOnBadLength()
	{
		new Hotel(-5);
	}

	
	@Test
    public void TestThatHotelDoesGetRoomOccupantFromTheDatabase()
    {
		
		IDatabase mockDB = EasyMock.createMock(IDatabase.class);
        String roomOccupant = "Whale Rider";
        String anotherRoomOccupant = "Raptor Wrangler";
        
        EasyMock.expect(mockDB.getRoomOccupant(24)).andReturn(roomOccupant);
        EasyMock.expect(mockDB.getRoomOccupant(1025)).andReturn(anotherRoomOccupant);

        // stub default return value
        EasyMock.expect(mockDB.getRoomOccupant(EasyMock.anyInt())).andStubReturn("Empty room");
        
        EasyMock.replay(mockDB);
       
        Hotel target = new Hotel(10);
        target.Database = mockDB;

        String result;


        result = target.getRoomOccupant(25);
        Assert.assertEquals("Empty room", result);

        result = target.getRoomOccupant(1025);
        Assert.assertEquals(anotherRoomOccupant, result);

        result = target.getRoomOccupant(24);
        Assert.assertEquals(roomOccupant, result);

        result = target.getRoomOccupant(23);
        Assert.assertEquals("Empty room", result);

        //mocks.VerifyAll();
        EasyMock.verify(mockDB);
    }
	
	@Test
	public void TestThatHotelDoesGetRoomCountFromDatabase()
	{
		IDatabase mockDatabase = new IDatabase()
			{
				public List<String> Passengers = null;
				public List<String> Rooms = null;
				public int Miles = 0;
				@Override
				public String getCarLocation(int carNumber) 
				{
				// TODO Auto-generated method stub
					return null;
				}
				@Override
				public String getRoomOccupant(int roomNumber) 
				{
					// TODO Auto-generated method stub
					return null;
				}
			};
		
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < 50; i++)
			list.add(i + "");
		mockDatabase.Rooms = list;
		Hotel target = new Hotel(10);
		
		target.Database = mockDatabase;
		Assert.assertEquals(50, target.AvailableRooms());
	}
	
	@Test
	public void TestThatHotelHasNoMiles()
	{
		Hotel target = new Hotel(10);
		
		Assert.assertEquals(0, target.getMiles());
	}

}
