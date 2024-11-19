BEGIN;
DO $$
BEGIN
    INSERT INTO action (action_id,title,initiator,datetime,"type","notify",message,"source",due_datetime,expiry_datetime,metadata,related_object) VALUES
    	 ('a53ef68c-31aa-4e48-923f-7078de5de2a7','Ordering a part','1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f','2024-09-19 12:37:44+05:30','alert',true,'Ordering a part of Transducer','0d9972a1-5715-4f88-a3b5-e00826ae81b1','2024-09-20 20:00:00+05:30','2024-09-30 20:00:00+05:30','{"product": "Transducer", "quantity": "1", "partNumber": "453611"}','https://www.google.com/'),
    	 ('1d74be1e-cef9-45de-95c7-e6a24234375e','Reduce temperature in control room','1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f','2024-09-19 12:45:04+05:30','alert',false,'The temperature in the control room is outside a threshold. Measured value 60 degrees centigrade','0d9972a1-5715-4f88-a3b5-e00826ae81b1','2024-09-30 18:39:05+05:30','2024-10-05 17:30:00+05:30','{"location": "L1234"}','https://www.google.com/'),
    	 ('6ca22435-1dd5-4441-8867-aad03424a322','Reduce temperature','1f5f4f59-e251-4f19-a6bf-1528eeb8bb2f','2024-09-19 13:19:33+05:30','alert',true,'The temperature in the control room is outside a threshold. Measured value 70 degrees centigrade','0d9972a1-5715-4f88-a3b5-e00826ae81b1','2024-09-25 18:39:05+05:30','2024-10-05 17:30:00+05:30','{"location": "L1015"}','https://www.google.com/');

    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;