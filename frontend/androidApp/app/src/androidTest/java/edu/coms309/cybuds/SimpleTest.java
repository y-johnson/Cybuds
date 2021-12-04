package edu.coms309.cybuds;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import edu.coms309.cybuds.model.User;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import com.google.common.truth.Truth.assertThat;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import edu.coms309.cybuds.model.User;
import edu.coms309.cybuds.network.HTTPDriver;


@RunWith(AndroidJUnit4.class)
public class SimpleTest {

    /*@Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertThat(HTTP_DRIVER.requestLogin(
                getBaseContext(),
                user,
                (response, context) -> {
                    User responseUser = new Gson().fromJson(response.toString(), User.class);

                    Toast.makeText(getBaseContext(), String.format("Welcome back, %s!", responseUser.getFirstName()), Toast.LENGTH_LONG).show();

                    Intent toProfile = new Intent(getBaseContext(), ProfileActivity.class);
                    toProfile.putExtra("currentUserProfile", responseUser);
                    startActivity(toProfile);
                }
        );).isTrue();
    }*/

    /*@Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertThat(HTTP_DRIVER.requestLogin(
                getBaseContext(),
                user,
                (response, context) -> {
                    User responseUser = new Gson().fromJson(response.toString(), User.class);

                    Toast.makeText(getBaseContext(), String.format("Welcome back, %s!", responseUser.getFirstName()), Toast.LENGTH_LONG).show();

                    Intent toProfile = new Intent(getBaseContext(), ProfileActivity.class);
                    toProfile.putExtra("currentUserProfile", responseUser);
                    startActivity(toProfile);
                }
        );).isTrue();
    }*/
/*
    @Test
    public void useAppContext() {
        // Context of the app under test.
        User user = new User();
        assertEquals(HTTP_DRIVER.requestLogin(
                getBaseContext(),
                user,
                (response, context) -> {
                    User responseUser = new Gson().fromJson(response.toString(), User.class);


                }
        ),1);
    }*/
}
