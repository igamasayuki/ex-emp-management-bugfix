(function () {
  const $addressSearchButton = document.getElementById("addressSearchButton");
  $addressSearchButton.addEventListener("click", () => {
    $.ajax({
      url: "http://zipcoda.net/api",
      dataType: "jsonp",
      data: {
        zipcode: $("#zipCode").val(),
      },
      async: true,
    })
      .done(function (data) {
        console.log($("#address").val(data.items[0].address));
        $("#address").val(data.items[0].address);
      })
      .fail(function (XMLHttpRequest, textStatus, errorThrown) {
        alert("正しい結果を得られませんでした");
        console.log("XMLHttpRequest:" + XMLHttpRequest.status);
        console.log("textStatus    :" + textStatus);
        console.log("errorThrown   :" + errorThrown.message);
      });
  });
})();
