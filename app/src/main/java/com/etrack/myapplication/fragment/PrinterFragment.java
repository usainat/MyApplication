package com.etrack.myapplication.fragment;


/*


public class PrinterFragment extends DialogFragment implements CallbackInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RECIPET = "recipet";
    private WebView wvPrint;
    private Button btPrint;
    private View root;
    private EpsonComDevice m_Device;
    private EpsonComDeviceParameters m_DeviceParameters;
    private EpsonComASBStatus m_ASBData;
    private String recipet;
    private Context mContext;
    private SalesOrder salesOrder;
    private MainActivity mHom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mHom = (MainActivity) getActivity();
        m_Device = new EpsonComDevice();
        m_DeviceParameters = new EpsonComDeviceParameters();
        //register myself as callback
        m_Device.registerCallback(this);
        connectToPrinter();
  //      Co.hideProgress(getActivity());
        if (getArguments() != null) {
            recipet = getArguments().getString(RECIPET);
            salesOrder = getArguments().getParcelable("recipetModel");
        }

    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void connectToPrinter() {
        EpsonCom.ERROR_CODE err = EpsonCom.ERROR_CODE.SUCCESS;

        //fill in some parameters
        m_DeviceParameters.PortType = EpsonCom.PORT_TYPE.ETHERNET;
        m_DeviceParameters.IPAddress = "192.168.0.88";
        m_DeviceParameters.PortNumber = 9100;

        if (err == EpsonCom.ERROR_CODE.SUCCESS) {
            //set the parameters to the device
            err = m_Device.setDeviceParameters(m_DeviceParameters);
            if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                String errorString = EpsonCom.getErrorText(err);
                messageBox(errorString, "SampleCode: setDeviceParameters Error", null);
            }
            if (err == EpsonCom.ERROR_CODE.SUCCESS) {
                //open the device
                err = m_Device.openDevice();
                //comment later
                //   err = EpsonCom.ERROR_CODE.SUCCESS;
                if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                    String errorString = EpsonCom.getErrorText(err);
                    Log.d("SampleCode", "Error from openDevice: " + errorString);
                    //    messageBox(errorString, "SampleCode 0: openDevice Error", null);
                    // mHom.onBackPressed();
                    showPrintFailureDialog();
                } else {
                    Spanned content = Html.fromHtml(recipet);
                //    ProgressUtils.startProgress(mContext,"print config", " Connecting to printer...",false);
                    connectAndPrint(content.toString());
                }
            }

            if (err == EpsonCom.ERROR_CODE.SUCCESS) {
                //activate ASB sending
                err = m_Device.activateASB(true, true, true, true, true, true);
                if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                    String errorString = EpsonCom.getErrorText(err);
                    Log.d("SampleCode", "Error from activateASB: " + errorString);
                    messageBox(errorString, "SampleCode 1: openDevice Error", null);
                }
            }
        }
    }

    private void showPrintFailureDialog() {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    connectToPrinter();
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    dialog.dismiss();
                }
            }
        };

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setTitle("Printer not connected");
        alertBuilder.setMessage("The KOT is not printed. Do you want to retry?");
        alertBuilder.setPositiveButton("Try Again in 5 sec", onClickListener);
        alertBuilder.setNegativeButton("Go back", onClickListener);
        alertBuilder.setCancelable(false);
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        WebSettings settings = wvPrint.getSettings();
        settings.setMinimumFontSize(18);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        wvPrint.setWebChromeClient(new WebChromeClient());

        //wb.loadUrl("file:///android_asset/inv.html");
        wvPrint.getSettings().setJavaScriptEnabled(true);
        wvPrint.loadDataWithBaseURL("", recipet, "text/html", "UTF-8", "");
        btPrint.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Spanned content = Html.fromHtml(recipet);
                ProgressUtils.startProgress(mContext,"Loading","Printing",false);
                connectAndPrint(content.toString());
            }
        });
        return root;
    }


    private void connectAndPrint(String a) {
        if (m_Device.isDeviceOpen() == false) {
            m_Device.openDevice();
        }

        EpsonCom.ERROR_CODE err = EpsonCom.ERROR_CODE.SUCCESS;

        EpsonCom.FONT font = EpsonCom.FONT.FONT_A;
        Boolean bold = false;
        Boolean underlined = false;
        Boolean doubleHeight = false;
        Boolean doubleWidth = false;

        try {

            font = EpsonCom.FONT.FONT_A;
//                    font = EpsonCom.FONT.FONT_B;
            bold = true;
//                bold = false;
//                underlined = true;
            underlined = false;
            //set double print
            switch (1) {
                case 1:
                    doubleHeight = false;
                    doubleWidth = false;
                    break;
                case 2:
                    doubleHeight = false;
                    doubleWidth = true;
                    break;
                case 3:
                    doubleHeight = true;
                    doubleWidth = false;
                    break;
                case 4:
                    doubleHeight = true;
                    doubleWidth = true;
                    break;
            }
            if (m_Device.isDeviceOpen() == true) {
                //comment later
                //   if (true) {
                //set print alignment
                switch (2) {
                    case 1:
                        err = m_Device.selectAlignment(EpsonCom.ALIGNMENT.LEFT);
                        if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                            String errorString = EpsonCom.getErrorText(err);
                            messageBox(errorString, "printString Error", null);
                        }
                        break;
                    case 2:
                        err = m_Device.selectAlignment(EpsonCom.ALIGNMENT.CENTER);
                        if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                            String errorString = EpsonCom.getErrorText(err);
                            messageBox(errorString, "printString Error", null);
                        }
                        break;
                    case 3:
                        err = m_Device.selectAlignment(EpsonCom.ALIGNMENT.RIGHT);
                        if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                            String errorString = EpsonCom.getErrorText(err);
                            messageBox(errorString, "printString Error", null);
                        }
                        break;
                }
                //comment later
                //    err = EpsonCom.ERROR_CODE.SUCCESS;
                if (err == EpsonCom.ERROR_CODE.SUCCESS) {
                    //print string
                    String sendString = a;
                    //   m_Device.printString(sendString, font, bold, underlined, doubleHeight, doubleWidth);
                    //    printTest();
                    printFormat();
                    //m_Device.sendData()
                    if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                        String errorString = EpsonCom.getErrorText(err);
                        messageBox(errorString, "printString Error", null);
                    }
                }
            //    Helper.hideProgress(mContext);
            } else {
         //       Helper.hideProgress(mContext);
                messageBox("Device is not open", "printString Error", null);
            }
        } catch (Exception e) {
         //   Helper.hideProgress(mContext);
            messageBox("Exception: " + e.toString() + " - " + e.getMessage(), "printString Error", null);
        }

    }


    private void printFormat() {
        DecimalFormat format = new DecimalFormat("###.000");
        ArrayList<SalesOrder> servicelist = item.getBilllist();
        String serviceName = "";
        String Qty = "";
        String price = "";
        String amount = "";// m_Device.printPage()
        String star = "";
        String s = "";
        try {
            s = new String("\u0628\u06A9".getBytes(), "UTF-8");
            star = new String("تعطي يونيكود رقما فريدا لكل حرف".getBytes(), "UTF-8");
            star = new String("تعطي يونيكود رقما فريدا لكل حرف".getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // star = String.format("%040x", new BigInteger(1, star.getBytes(Charset.forName("UTF-8"))));
        m_Device.printString(star, EpsonCom.FONT.FONT_A, true, true, true, true);
        m_Device.printString(star, EpsonCom.FONT.FONT_A, true, false, false, false);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.CENTER);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.CENTER);
        m_Device.printString(" Al-FAKHAMA TRAD & CONT. L.L.C", EpsonCom.FONT.FONT_A, true,
                false, false, false);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.LEFT);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.CENTER);
        m_Device.printString(" Tel 24505941 Faz 24505295", EpsonCom.FONT.FONT_A, false, false,
                false, false);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.CENTER);
        m_Device.printString(" Branch: Opp to Airport", EpsonCom.FONT.FONT_A, false, false,
                false, false);


        //  m_Device.printString("Store Location  : Termad Branch", EpsonCom.FONT.FONT_A, false, false, false, false);
        //  m_Device.printString("Store Contact no:000000" + billingModel.getContactNumber(), EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.printString("Bill no   :" + billingModel.getCustName(), EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.printString("Car no   :" + billingModel.getCarNumber(), EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.printString("Bill Date :" + billingModel.getCreatedDate(), EpsonCom.FONT.FONT_A, false, false, false, false);
        //   m_Device.printString(" كمية  |  السعر |كمية  |   وصف                ", EpsonCom.FONT.FONT_A, true, false, true, false);

        m_Device.printString("Description           | qty  | price  | Amount", EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.CENTER);
        m_Device.printString("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _", EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.LEFT);
        for (
                int i = 0;
                i < servicelist.size(); i++)

        {

            Integer Qtytest = servicelist.get(i).getQty();
            if (Qtytest > 0) {
                serviceName = servicelist.get(i).getServiceName();
                Qty = String.valueOf(servicelist.get(i).getQty());
                price = String.valueOf(format.format(servicelist.get(i).getPrice()));
                amount = String.valueOf(format.format(servicelist.get(i).getAmount()));
                //30 max
                int length = serviceName.length();
                if (serviceName.length() > 22) {
                    serviceName = serviceName.substring(0, 22) + "..";
                } else {
                    serviceName = rightPadding(serviceName, 22);
                }
                if (Qty.length() > 6) {
                    Qty = serviceName.substring(0, 6);
                } else {
                    Qty = rightPadding(Qty, 6);
                }
                if (price.length() > 8) {
                    price = serviceName.substring(0, 8);
                } else {
                    price = rightPadding(price, 8);
                }
                m_Device.printString(serviceName + " " + Qty + " " + price + " " + amount, EpsonCom.FONT.FONT_A, false, false, false, false);
                //   m_Device.printString("اسم الخدمة هناn", EpsonCom.FONT.FONT_A, false, false, false, false);
            }

        }
        //   m_Device.
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.LEFT);
        m_Device.printString("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _", EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.CENTER);
        m_Device.printString("Payable Amount OMR:" + format.format(billingModel.getNetamount()), EpsonCom.FONT.FONT_B, true, true, true, true);
        m_Device.printString("_______________________________________________", EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.LEFT);
        m_Device.printString("Bill Amount      OMR :" + format.format(billingModel.getTotalamount()), EpsonCom.FONT.FONT_A, false, false, false, false);
        //     m_Device.printString("Discount Amount  OMR :" + format.format(billingModel.getLessDiscount()), EpsonCom.FONT.FONT_A, false, true, false, false);
        //     m_Device.printString("Payable Amount   OMR :" + format.format(billingModel.getNetamount()), EpsonCom.FONT.FONT_A, false, true, false, false);
        m_Device.printString("Cash received    OMR :" + format.format(billingModel.getCashreceved()), EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.printString("Change Amount    OMR :" + format.format((billingModel.getCashreceved() - billingModel.getNetamount())), EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.RIGHT);
        //  m_Device.printString("(Discount   point  :" + billingModel.getLesspoint() + ")", EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.CENTER);
        m_Device.printString("_______________________________________________", EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.LEFT);
        m_Device.printString("This station is not responsible for damages that occur because of negligence or fault of the consumer ", EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.selectAlignment(EpsonCom.ALIGNMENT.CENTER);
        m_Device.printString("Thank you & Visit Again", EpsonCom.FONT.FONT_A, false, false, false, false);
        m_Device.cutPaper();

    }

   */
/* void printTextFile(String filename) {
        EpsonCom.ERROR_CODE err = EpsonCom.ERROR_CODE.SUCCESS;

        try {
            String text = readTextFile(filename);
            byte[] bs = text.getBytes("GB2312");

            if (m_Device.isDeviceOpen() == true) {
                Vector<Byte> data = new Vector<Byte>(bs.length);
                for (int i = 0; i < bs.length; i++) {
                    data.add(bs[i]);
                }

                err = m_Device.sendData(data);
                if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                    String errorString = EpsonCom.getErrorText(err);
                    messageBox(this, errorString, "cutPaper Error");
                } else {
                    err = m_Device.cutPaper();
                    if (err != EpsonCom.ERROR_CODE.SUCCESS) {
                        String errorString = EpsonCom.getErrorText(err);
                        messageBox(this, errorString, "cutPaper Error");
                    }
                }
            } else {
                messageBox(this, "Device is not open", "cutPaper Error");
            }
        } catch (Exception e) {
            messageBox(this, "Exception:" + e.getMessage(), "cutPaper Error");
        }
    }
*//*


    private String readTextFile(String filename) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final FileInputStream fs = new FileInputStream(new File(filename));
        final BufferedReader br = new BufferedReader(new InputStreamReader(fs));
        String s;
        try {
            while ((s = br.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
        } finally {
            if (br != null)
                br.close();
            if (fs != null)
                fs.close();
        }
        return sb.toString();
    }

   */
/* public void messageBox(final Context context, final String message, final String title) {
        PrinterFragment.runOnUiThread(
                new Runnable() {
                    public void run() {
                        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle(title);
                        alertDialog.setMessage(message);
                        alertDialog.setButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }


                        });
                        alertDialog.show();
                    }
                }
        );
    }*//*


    public static String rightPadding(String str, int num) {
        return String.format("%1$-" + num + "s", str);
    }

    public void messageBox(final String message, final String title, String type) {
      */
/*  mContext.runOnUiThread(
                new Runnable()
                {
                    public void run()
                    {*//*

        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });
        if (type != null && type.equals("retry")) {
            alertDialog.setButton("retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String a = Html.fromHtml(recipet).toString();
                    //String sendString =a;
                    connectAndPrint(a);
                }
            });
        }
        alertDialog.show();
              */
/*      }
                }
        );*//*


    }

    @Override
    public void onResume() {
        super.onResume();
        if (!m_Device.isDeviceOpen())
            m_Device.openDevice();
        hideKeyboard();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (m_Device.isDeviceOpen())
            m_Device.closeDevice();
    }

    @Override
    public EpsonCom.ERROR_CODE CallbackMethod(EpsonComCallbackInfo epsonComCallbackInfo) {
*/
/*
        EpsonCom.ERROR_CODE retval = EpsonCom.ERROR_CODE.SUCCESS;

        try
        {
            switch(cbInfo.ReceivedDataType)
            {
                case GENERAL:
                    //do nothing, ignore any general incoming data
                    break;
                case ASB:	//new ASB data came in
                    receiveAndShowASBData();
                    break;
                case MICR:	//new MICR data came in
                    receiveAndShowMICRData();
                    break;
                case IMAGE:	//new image data came in
                    receiveAndShowImageData();

                    //kill progress dialog
                    if(m_progressDialog!=null)
                    {
                        m_progressDialog.dismiss();
                    }
            }
        }
        catch(Exception e)
        {
            messageBox(this, "callback method threw exception: " + e.toString() + " - " + e.getMessage(), "Callback Error");
        }

        return retval;*//*

        return null;
    }

    private class Builder {
        public Builder(String s, Object p1) {
        }
    }
}*/
