# Questions

1. How long did you spend on the coding test? What would you add to your solution if you spent more time on it? If you didn't spend much time on the coding test then use this as an opportunity to explain what you would add.
	About 50 hours over 3 weeks. First few days I spent getting used to Intellij/Java and Ratpack. 
	I would of added further unit testing, testing the error handler which isn't covered at the minute, checking if SQL exceptions are caught and reported correctly.
	
	
2. What was the most useful feature that was added to Java 8? Please include a snippet of code that shows how you've used it.
	Lambdas, really find they make creating clean concise code easier.
	.path("", ctx -> {
		ctx.getResponse().status(200).send("Hello from MuchBetterAPI");
	})
	Method References added with lambda expressions alse look really clean when calling an existing method: 
	ctx.parse(fromJson(Transaction.class)).then(user::Spend);
	
3. What is your favourite framework / library / package that you love but couldn't use in the task? What do you like about it so much?
	I've recently been enjoying using WCF. A .NET framework for creating service oriented applications. 
	It's really handy for creating small services that act as a layer of abstraction infornt of the database. 
	You can host them in IIS and pass and recieve data asynchrously. They're pretty light weight and quite straight forward to set up.
	
4. What great new thing you learnt about in the past year and what are you looking forward to learn more about over the next year?
	Ratpack!(If that doesn't count as cheating). Have to say I really enjoyed writing the API using ratpack. 
	I thought the documentation was very straight forward with good examples. Jackson for parsing JSON was also impressive.
	Recently gotten into doing RegEx crosswords which hopefully I'll get better at in the next year.
	
5. How would you track down a performance issue in production? Have you ever had to do this? Can you add anything to your implementation to help with this?
	One of our WCF services which uploads large CSV files to the database began to take an age to finish.
	To track this down I started by checking logs. Then I tried to recreate the problem by finding the particular CSV it was tring to import from the logs and debugging.
	This led me one of the stored procedures it calls within the database. 
	Created new indexes, updated the statistics and modfied how it perfromed the INSERT Statement slightly and the proble was resolved.
	
6. How would you improve the APIs that you just used?
	Added further error handling. I think I could of follewed some more API best practices made further use of interfaces and reduced the code base.	
	
7. Please describe yourself in JSON format.
	{
		"Name": "Alex",
		"Age": "23",
		"Sport":"Cycling",
		"Interests":"Describing myself in JSON"
	}
	
8. What is the meaning of life?
	42