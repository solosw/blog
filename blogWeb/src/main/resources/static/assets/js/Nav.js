function getSignPage()
{
  $.ajax({
    url: '/sign',
    type: 'POST',
    success: function(response) {
    window.location.href=response;
  }
   
});
}
function getIndexPage()
{
  
  $.ajax({
    url: '/getindex',
    type: 'POST',
    success: function(response) {
    window.location.href=response;
  },
  error: function(xhr, status, error) {
    // 请求失败处理逻辑
    console.log("请求失败：" + error);
  }
});
}
function getLoginPage()
{
  $.ajax({
    url: '/login',
    type: 'POST',
    success: function(response) {
    window.location.href=response;
  },
  error: function(xhr, status, error) {
    // 请求失败处理逻辑
    console.log("请求失败：" + error);
  }
   
});
}
