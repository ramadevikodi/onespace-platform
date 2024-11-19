BEGIN;
DO $$
BEGIN
    INSERT INTO customer (customer_id, customer_name, description, address, email_contact, market_id)
    VALUES ('e0738dc3-792f-4640-a8c4-6f45298e1846', 'Apollo Hospitals',
            'Apollo Hospitals Enterprise Limited is an Indian multinational healthcare group headquartered in Chennai. It is the largest for-profit private hospital network in India, with a network of 71 owned and managed hospitals',
            'Bangalore', 'admin@apollo.com', (SELECT market_id FROM market WHERE country = 'SriLanka')),
           ('147efd18-d28c-4ece-92f8-b6b013c36eb3', 'Manipal Hospitals',
            'Manipal Health Enterprises, commonly known as Manipal Hospitals, is an Indian for-profit private hospital network headquartered in Bangalore, Karnataka',
            'Chennai', 'admin@manipal.com', (SELECT market_id FROM market WHERE country = 'SriLanka'));
 RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;        
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;