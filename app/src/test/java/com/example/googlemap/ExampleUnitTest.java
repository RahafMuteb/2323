package com.example.googlemap;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import com.example.googlemap.databinding.ActivityCreditCardBinding;
import com.example.googlemap.models.Locker;
import com.example.googlemap.utils.Globals;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Mock
    ActivityCreditCardBinding mockBinding;
    @Mock
    Locker mockLocker;
    @Mock
    FirebaseDatabase mockFirebaseDatabase;
    @Mock
    FirebaseAuth mockFirebaseAuth;
    CreditCardActivity creditCardActivity;
    @Before
    public void setUp() {
        Globals.selectedLocker = mockLocker;
        creditCardActivity = Mockito.spy(new CreditCardActivity());
        creditCardActivity.binding = mockBinding;

        // Mock Firebase dependencies if needed
        Mockito.when(FirebaseDatabase.getInstance()).thenReturn(mockFirebaseDatabase);
        Mockito.when(FirebaseAuth.getInstance()).thenReturn(mockFirebaseAuth);
    }
    @Test
    public void testPaymentConfirmation() throws InterruptedException {
        // Mock user input
        Mockito.when(mockBinding.editCardNumber.getText()).thenReturn(Editable.Factory.getInstance().newEditable("1234567890123456"));
        Mockito.when(mockBinding.editCardHolder.getText()).thenReturn(Editable.Factory.getInstance().newEditable("John Doe"));
        Mockito.when(mockBinding.editExpiryDate.getText()).thenReturn(Editable.Factory.getInstance().newEditable("12/24"));
        Mockito.when(mockBinding.editCVV.getText()).thenReturn(Editable.Factory.getInstance().newEditable("123"));

        // Mock the showAlertDialog method to avoid showing the actual dialog
        Mockito.doNothing().when(creditCardActivity).showAlertDialog();
        // Perform button click
        creditCardActivity.onClick(Mockito.mock(View.class));
        // Wait for the dialog to be shown (assuming it's shown with a delay)
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(5, TimeUnit.SECONDS);
        // Verify that the dialog is shown
        Mockito.verify(creditCardActivity).showAlertDialog();
        // Mock dialog button click
        DialogInterface dialogInterface = Mockito.mock(DialogInterface.class);
        // Use doAnswer to simulate the behavior of onClick
        Mockito.doAnswer(invocation -> {
            // Capture the provided DialogInterface.OnClickListener
            DialogInterface.OnClickListener listener = invocation.getArgument(1);
            // Simulate positive button click
            listener.onClick(dialogInterface, DialogInterface.BUTTON_POSITIVE);
            return null;
        }).when(creditCardActivity).showAlertDialog();
        // Verify that Firebase database is updated as expected
        Mockito.verify(mockFirebaseDatabase.getReference().child("post").child(Mockito.anyString()).child("booked_lockers").child(Mockito.anyString()).setValue(null));
        Mockito.verify(mockFirebaseDatabase.getReference().child("users").child(Mockito.anyString()).child("bookings").child(Mockito.anyString()).setValue(mockLocker));
        // Verify intent creation and start activity
        Intent expectedIntent = new Intent(creditCardActivity, HomeActivity.class);
        expectedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Mockito.verify(creditCardActivity).startActivity(expectedIntent);
    }


}
