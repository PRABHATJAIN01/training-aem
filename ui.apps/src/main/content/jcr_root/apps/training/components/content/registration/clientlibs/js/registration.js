$(document).ready(function() {

    $("#firstname").on("input", function(){
       $('#first_name_error').empty();
    }
  });

    $("#lastname").on("input", function(){
       $('#last_name_error').empty();
    });

  $('#submit').click(function(e) {
    e.preventDefault();

    //Get the user-defined values
    var firstName = $('#firstname').val();
    var lastName = $('#lastname').val();

    var hobbies = $('input[type="checkbox"]:checked').map(function() {
      return $(this).val();

    }).get().join(',');

    if (checkFirstNameError(firstName, lastName)) {

      //Use JQuery AJAX request to post data to a Sling Servlet
      $.ajax({
        type: 'POST',
        url: '/bin/registerTrainingServlet',
        data: 'firstName=' + firstName + '&lastName=' + lastName + '&hobbies=' + hobbies,
        success: function(msg) {

          setSuccessDiv(msg);

          document.getElementById("firstname").value = "";
          document.getElementById("lastname").value = "";
          const cbs = document.querySelectorAll('input[name="hobbies"]');
          cbs.forEach((cb) => {
            cb.checked = false;
          });
        }
      });
    }
  });

  $('#getAllData').click(function() {
    //Use JQuery AJAX request to post data to a Sling Servlet
    $.ajax({
      type: 'GET',
      url: '/bin/allRegistrationTrainingData',
      success: function(msg) {

        var registrationDiv = document.getElementById("registration-div");
        $('#registration-div').empty();

        setSuccessDiv(msg.message);

        if (msg.registrationData.length > 0) {

          registrationDiv.style.display = "block";
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

          for (var i = 0; i < msg.registrationData.length; i++) {

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

  function checkFirstNameError(firstName, lastName) {

    var isFirstNameAllOk = false;
    var isLastNameAllOk = false;
    var pattern = /^[a-zA-Z]+$/;

    if (firstName.trim().length < 1) {
      setFirstNameError("This field is required");

    } else if (!pattern.test(firstName.trim())) {
      setFirstNameError("Only Character required");

    } else {
      isFirstNameAllOk = true;
    }

    isLastNameAllOk = checkLastNameError(lastName);

    return (isFirstNameAllOk && isLastNameAllOk);
  }

  function checkLastNameError(lastName) {
    var isLastNameAllOk = false;
    var pattern = /^[a-zA-Z]+$/;

    if (lastName.trim().length < 1) {
      setLastNameError("This field is required");

    } else if (!pattern.test(lastName.trim())) {
      setLastNameError("Only Character required");

    } else {
      isLastNameAllOk = true;
    }

    return isLastNameAllOk;
  }

  function setFirstNameError(message) {

    $('#first_name_error').empty();
    $('#first_name_error').show();
    var span = document.getElementById('first_name_error');
    span.appendChild(document.createTextNode(message));
  }

  function setLastNameError(message) {

    $('#last_name_error').empty();
    $('#last_name_error').show();
    var span = document.getElementById('last_name_error');
    span.appendChild(document.createTextNode(message));
  }

  function setSuccessDiv(message) {
    $('#success-div').empty();
    $('#success-div').show();
    $('#success-div').append($('<div>').prop({
      innerHTML: message
    }));
    $("#success-div").delay(1000).fadeOut(500);
  }
}); // end ready