$(document).ready(function() {


$('.txtonly').keypress(function (e) {
			var regex = new RegExp("^[a-zA-Z]+$");
			var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
			if (regex.test(str)) {
				return true;
			}
			else
			{
			e.preventDefault();
			$('#errormsg').show();
            $('#errormsg').empty();

            var span = document.getElementById('errormsg');
            span.appendChild( document.createTextNode("Only Characters are allowed!"));
			return false;
			}
		});

  $("#countryName").on("input", function(){
       $('#errormsg').empty();

    }); 



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

                $('#errormsg').empty();
		        $('#errormsg').show();
		        var span = document.getElementById('errormsg');
                span.appendChild( document.createTextNode("Please Enter Valid 3 character Country Code"));

            }
        }
     });
  });
}); // end ready