Scalidate is a thin validation layer designed to be mixed into Plain Old Scala Objects.

## Motivation

The motivation behind the design of Scalidate is to be able to gracefully handle 
validation of user input and generating contextual error messages.

## Example

    import com.dmclaughlin.scalidation.Validation

    class User(
      val email: String, 
      val username: String, 
      val password: String,
      val dob: Date
    ) extends Validation {
      override def validate: Option[Failures] = {
        check("email", { s: String => s.trim.length > 0 }, "Must enter a valid email address.")
        check("username", { s: String => s.trim.length > 0 }, "Must enter a valid username.")
        check("password", { s: String => s.trim.length > 8 }, "Password must be at least 8 characters.") 
        check("dob", { d: Date => d.compareTo(new Date()) <= 0 }, "Sorry, no Terminators permitted.")
        failures  
      }
    }

    val u = new User("david@dmclaughlin.com", "david", "secret")
    val failures = u.validate match {
      case Some(failures) => failures
      case _ => None
    }
    
    failures.getFields // returns List("password")
    failures.get("password") // returns Some(List("Password must be at least 8 characters."))
 

Note that the validate method you implement in your class must return a type of Option[Failures].
This is the same type that is returned by the method failures in the Validation trait, so this
should always be the final call in your validate implementation. Note that since you are 
overriding an abstract method, it's safe to leave out the return type:

    override def validate = {
       check("email", { .. }, "Must enter a valid email address.")
       failures
    }

And the Scala compiler will still fail if you forget to include the call to failures. 

## check method

Check is a method of the Validation trait and it has the following signature:


    def check[T](fieldName: String, validator:T => Boolean, msg: String = "Error."):Boolean


The reasons it accepts a fieldName rather than a value of type T as an argument is so 
that it can set the correct key in the Failures object it sends any validation errors
to. Internally Validation uses reflection to look up the value of the fieldName you pass in.
The disadvantage of this approach is that we lose some type safety, since if you pass
in a fieldName that does not match a getter method on the object, it will generate a run
time exception. Basic testing of your validate method will catch this, however.

## TODO: Failures, Squeryl support, a full example of Scalatra/Squeryl/Scalidation 
