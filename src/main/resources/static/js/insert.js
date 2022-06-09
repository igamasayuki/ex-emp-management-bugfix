"user strict";
$(function () {
  $(document).on("keyup", "#confirmationPassword", function () {
    check_password();
  });

  function check_password() {
    let hostUrl = "http://localhost:8080/insert/check-password";
    let inputPassword = $("#password").val();
    let inputConfirmationPassword = $("#confirmationPassword").val();
    $.ajax({
      url: hostUrl,
      type: "POST",
      dataType: "json",
      data: {
        password: inputPassword,
        confirmationPassword: inputConfirmationPassword,
      },
      async: true,
    })
      .done(function (data) {
        console.log(data);
        console.dir(JSON.stringify(data));
        $("#disagreementMessage").html(data.disagreementMessage);
      })
      .fail(function (XMLHttpRequest, textStatus, errorThrown) {
        alert("エラーが発生しました。");
        console.log("XMLHttpRequest:" + XMLHttpRequest.status);
        console.log("textStatus :" + textStatus);
        console.log("errorThrown :" + errorThrown);
      });
  }
});
