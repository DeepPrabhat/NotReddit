# NotReddit

1.Signup Mehtod(/signup)
 takes data from RegisterRequest(dto) from requestBody and then pass it to User Object in signup In AuthService then user is saved in repository with status false 
 after that a token is generated using UUID.randomUUID then the token is sent to mailservice where we declare MimeMessageHelper and set To,From,Subject,text from 
 NotificationEmail Class.

2.Activation(/accountVerification)
takes token from @PathVariable then pass to VerifyAccount() which checks if the token in present in verification token table and if present finds the corresponding user and calls fetchUserAndEnable() which gets verifcationToken object we Find the user corresponding to the token and then if user is found we simply get that user from User(Table) and then we make setEnable=true to activate the account.
{We use @EnableAsync on SpringBootApplication to reduce time  required sending email as send email takes a lot of time and add @Async on the method that takes time
i.e sendMail()
other ways to implement this using Message Queue like RabbitMQ or ActiveMQ }
