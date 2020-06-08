$(document).ready(function() {

$('#submitCountryName').click(function() {
    var failure = function(err) {
             alert("Unable to retrive data "+err);
   };

    //Get the user-defined values
    var countryName= $('#countryName').val() ; 

    //Use JQuery AJAX request to post data to a Sling Servlet
    $.ajax({
         type: 'POST',
         url:'/bin/countryDetailsServlet',
         data:'countryName='+ countryName,
         success: function(msg){

       	    if(msg.status=='OK'){

        		 $('#country_data').empty();
        		 $('#country_data').append( $('<div>').prop({id:'innerdiv',innerHTML:"Name is:"+ msg.name }));
                 $('#country_data').append( $('<div>').prop({id:'innerdiv',innerHTML:"Capital is:"+ msg.capital}));
                 $('#country_data').append( $('<div>').prop({id:'innerdiv',innerHTML:"Demonym is:"+ msg.demonym}));

               // Create anchor element. 
                var a = document.createElement('a');  
                  
                // Create the text node for anchor element. 
                var link = document.createTextNode(msg.flag); 
                  
                // Append the text node to anchor element. 
                a.appendChild(link);  
                  
                // Set the title. 
                a.title = msg.flag;  
                  
                // Set the href property. 
                a.href = msg.flag;  

    			$('#country_data').append(a);


  		    }else{

                $('#country_data').empty();
           		alert("country initials are not right.");

            }
        }
     });
  });
}); // end ready