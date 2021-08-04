// eslint-disable-next-line no-undef
$(() => {
  // eslint-disable-next-line no-undef
  $.ajax({
    type: 'GET',
    url: 'pom.xml',
    complete: function (content) {
      // eslint-disable-next-line no-undef
      equo.send('_sendText', content.responseText)
    }
  })
})
