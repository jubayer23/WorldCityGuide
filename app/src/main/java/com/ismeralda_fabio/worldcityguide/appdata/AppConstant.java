package com.ismeralda_fabio.worldcityguide.appdata;

import com.ismeralda_fabio.worldcityguide.model.Place;
import com.ismeralda_fabio.worldcityguide.model.Review;
import com.ismeralda_fabio.worldcityguide.model.UserLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by ultimate on 5/8/2015.
 */
public class AppConstant {

    public static String url_search = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    public static String url_place_details = "https://maps.googleapis.com/maps/api/place/details/json?";

    public static String url_place_search = "https://maps.googleapis.com/maps/api/place/textsearch/json?";

    public static String url_photo_search = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";

    public static String url_photo_search_placelist = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";


    public static String url_distance = "https://maps.googleapis.com/maps/api/distancematrix/json?";

    public static String url_autocomplete = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";

    public static String radius = "1000";




    public static String[] API_KEY =
            {
                    "AIzaSyCHsF72opxJ7MfM5dqq4z_-2ujjujukI3E",
                    "AIzaSyBhjh1rMzVLUESCQI-jL8ZSiXsX649akD4",
                    "AIzaSyC4wfav1t9vSYFPyap0pShpb4KP7_hH9Gk",
                    "AIzaSyD4OsXwQ0d_p7nA3GV8b5ECas0IANZow20",
                    "AIzaSyCklmH7W3hGMLjfTyIHHMUM5iK8o3Xctqc",
                    "AIzaSyA2kmZ-LZVEMgwJtYVnEAi4UxahSWZa9Ns",
                    "AIzaSyDAefnKFENZZB-x5aYYqug6Il_z1WInIug"
            };

    //public static List<Place> staticPlaces;

    // public static UserLocation user_static_location;

    public static String topTitle = "NearByPlaces";

    public static boolean PLACE_SEARCH = false;

    public static UserLocation user_static_location;

    public static List<Place> staticPlaces;

    public static String des_lat = "";
    public static String des_lng = "";

    public static  int API_COUNTER = 1;

    public static List<Review> reviews = new ArrayList<Review>();


    public static HashMap<String, String> headers = new HashMap<String, String>();


    public static String query_type = "default_not_found";

    static {
        headers.put("X-Parse-Application-Id", "R6kBbmhFNsPv44ekZbLlC6hq7JZ7b4fWT5G3H3GN");
        headers.put("Content-Type", "application/json");
        headers.put("X-Parse-REST-API-Key", "QHh6SwA97ioIo8ZkmEczrpFr8jZB5G5rYybrlbpO");

    }

    //drawable/textview/queryTYPE
    public static String Cat_SubCat_Titles[][] = {
            {
                    "BANKS and ATMS",
                    "atm/ATM/atm",
                    "bank/Banks/bank"

            },
            {
                    "FOOD and BEVERAGES",
                    "cafe/Cafes/cafe",
                    "food/Foods/food|restaurant",
                    "restaurent2/Restaurant/restaurant",
                    "bekary/Bakery/bakery",
                    "mealdelivery/MealDelivery/meal_delivery",
                    "mealdelivery/MealTakeWay/meal_takeway"
            },
            {
                    "HEALTH",
                    "dentist/Dentists/dentist",
                    "doctor/Doctors/doctor",
                    "hospital2/Hospitals/hospital",
                    "pharmecy2/Pharmacy/pharmacy",
                    "gym/GYMs/gym",
                    "veterinarian/Veterinarian/veterinarian",
                    "physiotherapist/Physiotherapist/physiotherapist"
            },
            {
                    "STORE",
                    "book/BookStores/book_store",
                    "store/Stores/store",
                    "clothing_store/Clothes/clothing_store",
                    "pet_store/PetStores/pet_store",
                    "hardware_store/Hardwares/hardware_store",
                    "electric_store/Electrics/electronics_store",
                    "bycycle_store/Bicycles/bicycle_store",
                    "convenience_store/Convenience/convenience_store",
                    "liquor_store/Liquors/liquor_store",
                    "mall/Malls/shoping_mall",
                    "grocery/Groceries/grocery_or_supermarket",
                    "shoe_store/ShoeStore/shoe_store",
                    "jewelry/JewelryStore/jewelry_store",
                    "florist/Florist/florist",
                    "furniture/Furniture/furniture_store",
                    "general_contractor/GeneralContractor/general_contractor",
                    "store/HomeGoodStore/home_goods_store"
            },
            {
                    "CARs",
                    "carwash/CarWash/car_wash",
                    "gas/GasStations/gas_station",
                    "car_dealer/CarDealers/car_dealer",
                    "car_rental/CarRentals/car_rental",
                    "car_repair/CarRepairs/car_repair",
                    "car_paint/CarPaint/car_wash",
                    "park/RvPark/rv_park"
            },
            {
                    "TRANSPORTING and TRAVEL",
                    "bus/BusStations/bus_station",
                    "airport/Airport/airport",
                    "texi_stand/TaxiStands/taxi_stand",
                    "subway_station/SubWays/subway_station",
                    "train/TrainStation/train_station",
                    "travel_agency/TravelAgency/travel_agency"
            },
            {
                    "EDUCATION",
                    "university2/Universities/university",
                    "school/School/school",
                    "libraray/Library/library"
            },
            {
                    "BEAUTY",
                    "spa/Spa/spa",
                    "salun/HairCare/hair_care",
                    "salun/BeautySalon/beauty_salon"
            },
            {
                    "ENTERTAINMENT",
                    "park/Parks/park",
                    "threater2/Theaters/movie_theater",
                    "night_club/NightClub/night_club",
                    "bar/Bars/bar",
                    "zoo/Zoos/zoo",
                    "amusement/Amusement/amusement_park",
                    "casino/Casino/casino"
            },
            {
                    "RELIGIOUS INSTITUTE",
                    "mosque/Mosque/mosque",
                    "hindu_temple/HinduTemple/hindu_temple",
                    "church/Church/chruch",
                    "cemetery/Cemetery/cemetery",
                    "funeral_home/FuneralHome/funeral_home"
            },
            {
                    "LAW INSTITUTE",
                    "police/Police/police",
                    "lawyer/Lawyer/lawyer",
                    "court_house/Courthouse/courthouse"
            },
            {
                    "SERVICE",
                    "fire_station/FireStation/fire_station",
                    "paint/Painter/painter",
                    "plumber/Plumber/plumber",
                    "electrician/Electrician/electrician",
                    "laundry/Laundry/laundry",
                    "roofing_contractor/RoofingContractor/roofing_contractor"
            },
            {
                    "HOTEL & ACCOMODATIONS",
                    "lodging/Lodging/lodging",
                    "hotel/Hotel/hotel",
                    "motel/Motel/motel",
                    "city_hall/CityHall/city_hall"
            },
            {
                    "SPORTS",
                    "bowling_alley/BowlingAlley/bowling_alley",
                    "stadium/Stadium/stadium",
                    "swimming/Swimming/swimming",
                    "campground/Campground/campground"
            },
            {
                    "GOVT. ORGANISATION",
                    "finance/Finance/finance",
                    "accounting/Accounting/accounting",
                    "local_government_office/GovtOffice/local_government_office",
                    "insurance_agency/InsuranceAgency/insurance_agency",
                    "real_estate_egency/RealEstateAgency/real_estate_agency"
            }
    };
}
