$(document).ready(function(){

   document.onkeyup = function() {

    $(".error").remove();


   }

$('#submit').click(function() {
    var failure = function(err) {
             alert("Unable to retrive data "+err);
   };

    //Get the user-defined values
    var firstName= $('#firstname').val() ; 
    var lastName= $('#lastname').val() ; 

    var hobbies = $('input[type="checkbox"]:checked').map(function() {
      return $(this).val();
    }).get().join(',');

    document.body.append(hobbies);


    $(".error").remove();

    if (firstName.length < 1) {
      $('#firstname').after('<span class="error">This field is required</span>');
    }
   else if (lastName.length < 1) {
      $('#lastname').after('<span class="error">This field is required</span>');
    }
    else{

    //Use JQuery AJAX request to post data to a Sling Servlet
    $.ajax({
         type: 'POST',
         url:'/bin/registerTrainingServlet',
         data:'firstName='+ firstName+'&lastName='+ lastName+'&hobbies='+ hobbies,
         success: function(msg){

        	$('#success-div').empty();
            $('#success-div').show();
            $('#success-div').append($('<div>').prop({innerHTML:msg }));
			$("#success-div").delay(1000).fadeOut(500); 

         }
     });
    }
  });

    $('#getAllData').click(function(){
         var failure = function(err) {
             alert("Unable to retrive data "+err);
         }


         //Use JQuery AJAX request to post data to a Sling Servlet
  	     $.ajax({
         type: 'GET',
         url:'/bin/allRegistrationTrainingData',
         success: function(msg){

             var trHTML = '';

             if(msg.registrationData.length>0){
                  var registrationDiv = document.getElementById("registration-div");
			  registrationDiv.style.display="block";
             }


              $('#success-div').empty();
              $('#success-div').show();
              $('#success-div').append($('<div>').prop({innerHTML:msg.message }));
			  $("#success-div").delay(1000).fadeOut(500);


               for(var i = 0; i < msg.registrationData.length; i++){

                    trHTML += '<tr><td>' + msg.registrationData[i].firstName + '</td><td>' + msg.registrationData[i].lastName + '</td><td>' + msg.registrationData[i].hobbies + '</td></tr>';

               }

               $('#registration-table').append(trHTML);

         }
     });
    });

}); // end ready