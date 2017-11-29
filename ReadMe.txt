POST /user/register		 (@RequestBody User user)
POST /user/register/fromFile 		(@RequestParam MultipartFile file)
POST /user/remove  		 (@RequestBody User user)
GET  /user/get/{id}
GET  /user/get/{email}
GET  /user/get/{name}
GET  /user/ticket/get?userId=1
GET  /user/ticket/get?userId=1		(accept=application/pdf)
GET  /user/file/upload

GET  /booking/ticket/price/get?eventName=eventName&auditoriumName=auditoriumName&date=12345678&seats={1,2,3,4,5}&userId=1
GET  /booking/ticket/get?eventName=eventName&auditoriumName=auditoriumName&date=1234567
GET  /booking/ticket/get?eventName=eventName&auditoriumName=auditoriumName&date=1234567  (headers = "accept=application/pdf")
POST /booking/ticket/book?userId=1 	(@RequestBody Ticket ticket)