BEGIN;
DO $$
BEGIN
    INSERT INTO customer_external_system_mapping (customer_id, hsp_iam_org_id, sap_account_id)
    VALUES ('e0738dc3-792f-4640-a8c4-6f45298e1846', 'eafc455d-b3ed-4c72-85bd-fc176cf13e19', 'CUST0001'),
           ('147efd18-d28c-4ece-92f8-b6b013c36eb3', '2c235e1a-d407-4241-b502-8b7053e58171', 'CUST0002');
     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;