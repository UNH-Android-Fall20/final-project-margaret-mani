package edu.newhaven.virtualfarmersmarket

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object Mailer {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    private fun sendMessageTo(emailFrom: String, session: Session, message: String, subject: String, emailTo: String) {
        session.debug = true
        try {
            MimeMessage(session).let { mime ->
                mime.setFrom(InternetAddress(emailFrom))
                // Adding receiver
                mime.addRecipient(Message.RecipientType.TO, InternetAddress(emailTo))
                // Adding subject
                mime.subject = subject
                // Adding message
                mime.setText(message)
                // Set Content of Message to Html if needed
                mime.setContent(message, "text/html")
                // send mail
                Transport.send(mime)
            }

        } catch (e: MessagingException) {
            Log.i("MessagingException", e.toString()) // Or use timber, it really doesn't matter
        }
    }

    fun sendMail(emailTo: String) {

        val props = Properties()
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        props["mail.smtp.ssl.enable"] = true
        props["mail.smtp.auth"] = "true"
        props["mail.user"] = "virtualfarmersmarketdev@gmail.com"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.ssl.trust"] = "smtp.gmail.com"
        props["mail.mime.charset"] = "UTF-8"

        // Open a session
        val session = Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("virtualfarmersmarketdev@gmail.com", "mani@margaret99")
            }
        })

        // Create a message
        val message = "Hi Seller, " +
                "Greetings from VIRTUAL FARMERS MARKET !!" +
                "email."

        // Create subject
        val subject = "First test email"

        // Send Email
        CoroutineScope(Dispatchers.IO).launch { sendMessageTo("virtualfarmersmarketdev@gmail.com", session, message, subject, emailTo) }
    }

}