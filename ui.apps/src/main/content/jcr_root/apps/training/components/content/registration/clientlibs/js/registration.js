$(document).ready(function(){
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


            alert(msg); 
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

             for(var i = 0; i < msg.registrationData.length; i++){

                 $('#registration-data').append( $('<div>').prop({id:'innerdiv'+i,innerHTML:"First name:"+ msg.registrationData[i].firstName }));
                 $('#registration-data').append( $('<div>').prop({id:'innerdiv'+i,innerHTML:"Last name:"+ msg.registrationData[i].lastName }));
                 $('#registration-data').append( $('<div>').prop({id:'innerdiv'+i,innerHTML:"Hobbies:"+ msg.registrationData[i].hobbies }));
                 $('#registration-data').append( $('<div>').prop({id:'innerdiv'+i,innerHTML:"-------" }));

             }

         }
     });
    });

}); // end ready
