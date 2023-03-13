# NotReddit

1.Signup Mehtod(/signup)
 takes data from RegisterRequest(dto) from requestBody and then pass it to User Object in signup In AuthService then user is saved in repository with status false 
 after that a token is generated using UUID.randomUUID then the token is sent to mailservice where we declare MimeMessageHelper and set To,From,Subject,text from 
 NotificationEmail Class.
