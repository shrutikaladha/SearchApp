# SearchApp
Android app to search the keywords using Android JetPack components: LiveData, ViewModel, Paging Library and Retrofit.

## Implementation:
The app comprises of folllowing modular components:

#### Network
- Created a RestApiFactory to initialize the Retrofit instance. Added an OkHttpInterceptor to log all request and responses.
- Created a RestApi interface to define the network calls with query params and return arguments.

#### Data
- Factory
Created a FeedDataFactory which would feed data from FeedDataSource based on user scroll by requesting to network. As an extension, we can add an intermediate local Database to give faster responses if required.
- Database
Created a database as RecentSuggestionsProvider to save user's latest searches and show in suggestions.

#### UI
- ViewModel
Created a ViewModel to update the SearchActivity with the LiveData received from FeedDataSource and send a callback to Adapter to update the views.
- Adapter
Created SearchListAdapter which is an extension of PagedListAdapter to implement pagination on basis of user scroll.
- Activity detailing

#### Model
- Defined model classes here for Photos, Photo and SearchModel which is created as per the json format in which API returns the data.

#### AppController
- Created an AppController class which is an extension of Application class to initialize library and rest interfaces. 
