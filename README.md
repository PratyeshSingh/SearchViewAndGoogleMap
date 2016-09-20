

1.      Provide a search box to enter text and integrate with the existing online webservice (mentioned below) to fetch a list of matching states using code or free text . When the state is found show the state details else report incorrect state.
http://services.groupkt.com/state/search/IND?text=<search-text>

Example : Searching for “pradesh” should make an api call to:

      http://services.groupkt.com/state/search/IND?text=pradesh

and provide list of all matching states.

2.                  Provide another search box which lets you enter an ip address. Make a call to given ip address which will return the location based on the ip provided. Show this location on google maps.
http://geo.groupkt.com/ip/<ip-address>/json
 
App should be compliant with material design and have an excellent UI.
