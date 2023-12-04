Learning Managamenet System API - Java Springboot PROPOSAL PROJECT

This project will create a Learning Management System API which has features including:

Modules / Services :
- Auth Service
	- Register
	- Login
	- Logout
	- Refresh Token
	- Activated User
	- Social auth (optional

- Course Service
	- Create Course
	- Update Course by id
	- Get course by id -- withouth purchasing
	- Get all courses -- only admin
	- Get courses content (list video course) -- on user after purchase the course
	- Add question to particular course content
	- Add Comment/reply question inside particular course content
	- Add review to course in general
	- Add reply to review -- only admin
	- Delete course -- only admin

- Order Service
	- Create Order
	- Get all Orders on particula course -- only admin

- Notification Service
	- Get all Notification
	- Notification Status

- User Service
	- Get all users -- only admin
	- Update user role -- only admin
	- Get user that currently login
	- Update user info
	- Update avatar profile
	- Delete user by id -- only admin
	- Update password

- Analitycs (Optional)
	- Get users analitycs -- only admin
	- Get courses analitycs -- only admin
	- Get orders analitycs -- only admin


SOME TECHNOLOGY THAT WILL IMPLEMENT
- JWT auth
- Access token
- Refresh token
- Social Media auth
- Activation Account
- MySql/Postgres DB
- SMTP send email
- Cloudinary / AWS cloud Storage
- Notification
- Redis
