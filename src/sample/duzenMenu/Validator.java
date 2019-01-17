package sample.duzenMenu;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {


    public void enterOnlyDigits(JFXTextField jfxTextField){

        jfxTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("[0-9]{0,11}")) {
                    jfxTextField.setText(oldValue);
                }
            }
        });
    }


    public void enterOnlyFloat(JFXTextField jfxTextField){

        jfxTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("[0-9]*(\\.[0-9]*)?")) {
                    jfxTextField.setText(oldValue);
                }
            }
        });
    }


    public void JfxTextFieldisEmptyValidator(JFXTextField jfxtextField){

        RequiredFieldValidator validator = new RequiredFieldValidator();

        jfxtextField.getValidators().add(validator);
        validator.setMessage("Cannot be empty");

        jfxtextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    jfxtextField.validate();
                }
            }
        });


        Image icon= null;
        try {

            icon = new Image(new FileInputStream("xIcon.png"));
            validator.setIcon(new ImageView(icon));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }



    public void JfxTextAreaisEmptyValidator(JFXTextArea jfxTextArea){

        RequiredFieldValidator validator = new RequiredFieldValidator();

        jfxTextArea.getValidators().add(validator);
        validator.setMessage("Cannot be empty");

        jfxTextArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    jfxTextArea.validate();
                }
            }
        });


        Image icon= null;
        try {
            icon = new Image(new FileInputStream("xIcon.png"));
            validator.setIcon(new ImageView(icon));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }



}
