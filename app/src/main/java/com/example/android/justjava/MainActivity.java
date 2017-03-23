package com.example.android.justjava;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.CheckBox;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int quantity = 1; // cantidad de cafés
    int cupPrice = 5; //precio por defecto
    int creamPrice = 1; // precio topping crema
    int chocolatePrice = 2; // precio topping chocolate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitOrder(View view) {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        EditText editText = (EditText) findViewById(R.id.name_imput);
        String userName = editText.getText().toString();;

        //checking for empty input
        if (userName.trim().isEmpty()){
            Toast.makeText(this, getString(R.string.no_name_error),Toast.LENGTH_SHORT).show();
            return;
        }

        String data = createOrderSummary(calculatePrice(hasWhippedCream, hasChocolate),hasWhippedCream, hasChocolate, userName);
        emailSent(data,userName);
    }

    private void emailSent (String data, String userName){
        String subject = getString(R.string.order_summary_subject, userName.trim());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate){
        int totalPrice = 0;

        if (hasWhippedCream ) {
            totalPrice = quantity * (cupPrice + creamPrice);
        }

        if (hasChocolate) {
            totalPrice = quantity * (cupPrice + chocolatePrice);
        }
        return totalPrice;
    }

    public void increment(View view) {
        if (quantity == 100){
            Toast.makeText(this, getString(R.string.quantity_error_100),Toast.LENGTH_SHORT).show();
            return;
        }
        quantity++;
        displayQuantity(quantity);
    }

    public void decrement(View view) {
        if (quantity == 1){
            Toast.makeText(this, getString(R.string.quantity_error_0),Toast.LENGTH_SHORT).show();
            return;
        }
        quantity--;
        displayQuantity(quantity);
    }

    private String createOrderSummary(int totalPrice, boolean hasWhippedCream, boolean hasChocolate, String userName){
        String priceMessage = getString(R.string.order_summary_name, userName.trim());
        priceMessage += "\n" + getString(R.string.order_summary_whipped_cream, hasWhippedCream);
        priceMessage += "\n" + getString(R.string.order_summary_chocolate, hasChocolate);
        priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage += "\n" + getString(R.string.order_summary_price, totalPrice) + getString(R.string.euro);
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }
}