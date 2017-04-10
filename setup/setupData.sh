curl -H "Content-Type: application/json" -X POST http://127.0.0.1:9000/v1.0/customers -d @customerOne.json
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:9000/v1.0/address -d @addressOne.json
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:9000/v1.0/address -d @addressTwo.json
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:9000/v1.0/customers -d @customerTwo.json
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:9000/v1.0/address -d @addressThree.json
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:9000/v1.0/invoices/ -d @paymentInvoiceForCustomerOneAndAddressOne.json
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:9000/v1.0/invoices/ -d @shopInvoiceForCustomerOneAndAddressTwo.json
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:9000/v1.0/invoices/ -d @paymentInvoiceForCustomerTwoAndAddressThree.json
curl -H "Content-Type: application/json" -X POST http://127.0.0.1:9000/v1.0/invoices/ -d @shopInvoiceForCustomerTwoAndAddressOne.json

