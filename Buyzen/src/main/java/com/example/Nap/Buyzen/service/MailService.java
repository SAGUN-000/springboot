package com.example.Nap.Buyzen.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(String to) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Welcome to Buyzen!");

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            padding: 40px;
                        }

                        .container {
                            max-width: 600px;
                            margin: auto;
                            background-color: white;
                            padding: 30px;
                            border-radius: 10px;
                        }

                        h1 {
                            color: #333333;
                        }

                        p {
                            color: #555555;
                            font-size: 16px;
                            line-height: 1.6;
                        }

                        .footer {
                            margin-top: 30px;
                            font-size: 13px;
                            color: #999999;
                        }
                    </style>
                </head>

                <body>
                    <div class="container">
                        <h1>Welcome to Buyzen! 🎉</h1>

                        <p>
                            Your account has been successfully created.
                        </p>

                        <p>
                            Thanks for registering with Buyzen. You can now
                            log in and start using the platform.
                        </p>

                        <div class="footer">
                            <p>
                                This is an automated email. Please do not reply.
                            </p>
                            <p>© 2026 Buyzen</p>
                        </div>
                    </div>
                </body>
                </html>
                """;

            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send registration email", e);
        }
    }
}
