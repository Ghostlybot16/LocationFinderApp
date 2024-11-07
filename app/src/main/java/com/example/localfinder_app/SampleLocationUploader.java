package com.example.localfinder_app;

import com.example.localfinder_app.DatabaseFunctions.Location;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SampleLocationUploader {

    // Method to add sample locations to Firebase
    public void addSampleLocations() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Locations");

        // Array of locations from GTA
        String[] locations = {
                "CN Tower,43.6426,-79.3871",
                "Royal Ontario Museum,43.6677,-79.3948",
                "Toronto Islands,43.6205,-79.3783",
                "Ripley's Aquarium of Canada,43.6424,-79.3860",
                "Casa Loma,43.6780,-79.4094",
                "St. Lawrence Market,43.6487,-79.3716",
                "High Park,43.6465,-79.4637",
                "Ontario Science Centre,43.7165,-79.3388",
                "Distillery District,43.6503,-79.3596",
                "Toronto Zoo,43.8200,-79.1803",
                "Eaton Centre,43.6544,-79.3807",
                "Nathan Phillips Square,43.6525,-79.3839",
                "University of Toronto (St. George Campus),43.6629,-79.3957",
                "Art Gallery of Ontario,43.6536,-79.3925",
                "Yonge-Dundas Square,43.6562,-79.3807",
                "Harbourfront Centre,43.6387,-79.3815",
                "Trinity Bellwoods Park,43.6469,-79.4172",
                "The Beaches,43.6727,-79.2966",
                "Toronto Pearson International Airport,43.6777,-79.6248",
                "Downsview Park,43.7482,-79.4823",
                "Toronto Botanical Garden,43.7346,-79.3646",
                "Scarborough Bluffs Park,43.7054,-79.2310",
                "Allan Gardens,43.6629,-79.3725",
                "Bata Shoe Museum,43.6676,-79.4007",
                "Evergreen Brick Works,43.6846,-79.3650",
                "Toronto Public Library (Toronto Reference Library),43.6711,-79.3868",
                "BMO Field,43.6330,-79.4185",
                "Queen’s Park,43.6629,-79.3925",
                "Guild Park and Gardens,43.7394,-79.2008",
                "Woodbine Beach,43.6644,-79.2989",
                "Aga Khan Museum,43.7256,-79.3382",
                "Sugar Beach,43.6432,-79.3700",
                "Rogers Centre,43.6414,-79.3894",
                "Fort York National Historic Site,43.6393,-79.4064",
                "Ontario Place,43.6289,-79.4144",
                "Kensington Market,43.6545,-79.4001",
                "Sunnybrook Park,43.7239,-79.3610",
                "Rouge National Urban Park,43.8058,-79.1756",
                "Edwards Gardens,43.7358,-79.3621",
                "Nathan Phillips Square Skating Rink,43.6525,-79.3839",
                "Scarborough Town Centre,43.7755,-79.2576",
                "Sherway Gardens,43.6098,-79.5580",
                "Don Valley Brick Works Park,43.6866,-79.3648",
                "Centennial Park Conservatory,43.6505,-79.5855",
                "St. Lawrence Market,43.6496,-79.3716",
                "The Beaches Boardwalk,43.6709,-79.2965",
                "Spadina Museum,43.6774,-79.4072",
                "Yorkdale Shopping Centre,43.7255,-79.4524",
                "Toronto Islands Ferry Terminal,43.6407,-79.3737",
                "East Point Park,43.7434,-79.1835",
                "David Dunlap Observatory,43.8412,-79.4240",
                "The Elgin and Winter Garden Theatre Centre,43.6545,-79.3791",
                "High Park Zoo,43.6466,-79.4656",
                "R.C. Harris Water Treatment Plant,43.6735,-79.2877",
                "Trinity Bellwoods Park,43.6487,-79.4154",
                "Canada's Wonderland,43.8430,-79.5393",
                "Square One Shopping Centre,43.5936,-79.6454",
                "Toronto City Hall,43.6535,-79.3840",
                "Yonge-Dundas Square,43.6562,-79.3807",
                "Metro Toronto Zoo,43.8177,-79.1859",
                "Rogers Centre,43.6415,-79.3890",
                "Exhibition Place,43.6335,-79.4180",
                "Toronto Music Garden,43.6387,-79.3964",
                "Toronto Public Library - Toronto Reference Library,43.6717,-79.3863",
                "Ryerson University,43.6577,-79.3788",
                "Ontario Science Centre,43.7160,-79.3380",
                "Scotiabank Arena,43.6435,-79.3791",
                "Toronto Police Museum,43.6625,-79.3844",
                "Gibson House Museum,43.7696,-79.4118",
                "Nathan Phillips Square,43.6525,-79.3832",
                "Michael Garron Hospital,43.6905,-79.3231",
                "Mount Pleasant Cemetery,43.6977,-79.3830",
                "Sunnybrook Park,43.7226,-79.3584",
                "Toronto Botanical Garden,43.7341,-79.3633",
                "Aga Khan Museum,43.7257,-79.3464",
                "Woodbine Racetrack,43.7167,-79.6015",
                "Richmond Hill Centre for the Performing Arts,43.8417,-79.4305",
                "Canada Christian College,43.7276,-79.3408",
                "Japanese Canadian Cultural Centre,43.7270,-79.3394",
                "Edward Gardens,43.7346,-79.3594",
                "Thornhill Community Centre,43.8232,-79.4209",
                "Harbourfront Centre,43.6370,-79.3817",
                "St. Michael's Cathedral Basilica,43.6548,-79.3762",
                "Little Italy,43.6556,-79.4175",
                "Bata Shoe Museum,43.6677,-79.4003",
                "Humber Bay Arch Bridge,43.6353,-79.4789",
                "Downsview Park,43.7428,-79.4789",
                "BMO Field,43.6354,-79.4184",
                "Old Mill Toronto,43.6502,-79.4943",
                "Port Union Waterfront Park,43.7854,-79.1311",
                "Richmond Green Sports Centre and Park,43.8771,-79.3867",
                "Don Mills Shops at Don Mills,43.7366,-79.3443",
                "The Junction,43.6656,-79.4644",
                "Parkway Mall,43.7592,-79.3112",
                "Scarborough Bluffs,43.7054,-79.2372",
                "Billy Bishop Toronto City Airport,43.6275,-79.3962",
                "York University,43.7735,-79.5019",
                "Humber College - North Campus,43.7292,-79.6088",
                "Rouge National Urban Park,43.8050,-79.1376",
                "Kensington Market,43.6550,-79.4020"
        };

        // Loop through each item in the locations array
        for (int i = 0; i < locations.length; i++) {

            // Get the current location entry from the array as a single string
            String loc = locations[i];

            // Split the location string by commas to separate address, latitude and longitude
            String[] parts = loc.split(",");

            // Assign the first part as the address
            String address = parts[0];

            // Convert the 2nd and 3rd part (latitude & longitude) from String to double and assign it as their name
            double latitude = Double.parseDouble(parts[1]);
            double longitude = Double.parseDouble(parts[2]);

            // Use the loop index (i+1) as the ID
            String id = String.valueOf(i + 1); // Sequential IDs starting from 1

            // New location object with the extracted address, latitude and longitude values
            Location location = new Location(address, latitude, longitude);

            // Add this location entry to the FirebaseDB under the generated ID values
            databaseReference.child(id).setValue(location);
        }
    }
}
