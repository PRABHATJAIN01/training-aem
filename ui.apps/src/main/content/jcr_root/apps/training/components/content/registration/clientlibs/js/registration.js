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

              var registrationDiv = document.getElementById("registration-div");
              $('#registration-div').empty();
              $('#success-div').empty();
              $('#success-div').show();
              $('#success-div').append($('<div>').prop({innerHTML:msg.message }));
			  $("#success-div").delay(1000).fadeOut(500);

             if(msg.registrationData.length>0){

			     registrationDiv.style.display="block";
                 var table = document.createElement('table');
                 table.style.cssText = 'width:100%';
                 table.setAttribute('border', '1');
                 var mainRow = document.createElement('tr');

                 var th1 = document.createElement('th');
                 var th2 = document.createElement('th');
                 var th3 = document.createElement('th');

                 var firstNameHeading = document.createTextNode('First Name');
                 var lastNameHeading = document.createTextNode('Last Name');
                 var hobbiesHeading = document.createTextNode('Hobbies');

                 th1.appendChild(firstNameHeading);
                 th2.appendChild(lastNameHeading);
                 th3.appendChild(hobbiesHeading);

                 mainRow.appendChild(th1);
                 mainRow.appendChild(th2);
                 mainRow.appendChild(th3);

                 table.appendChild(mainRow);

                 for(var i = 0; i < msg.registrationData.length; i++){

                     var tr = document.createElement('tr');

                     var td1 = document.createElement('td');
                     var td2 = document.createElement('td');
                     var td3 = document.createElement('td');

                     var text1 = document.createTextNode(msg.registrationData[i].firstName);
                     var text2 = document.createTextNode(msg.registrationData[i].lastName);
                     var text3 = document.createTextNode(msg.registrationData[i].hobbies);

                     td1.appendChild(text1);
                     td2.appendChild(text2);
                     td3.appendChild(text3);

                     tr.appendChild(td1);
                     tr.appendChild(td2);
                     tr.appendChild(td3);

                     table.appendChild(tr);
                }

                 registrationDiv.appendChild(table);


             }
         }
     });
    });

}); // end ready