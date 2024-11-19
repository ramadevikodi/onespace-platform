BEGIN;
DO $$
BEGIN
    INSERT INTO action_owners (action_owner_id,action_id,potential_owner,status,completed_at_datetime) VALUES
    	 ('778f0fe8-8b0f-4447-9ff1-6cdc264ec299','a53ef68c-31aa-4e48-923f-7078de5de2a7','e42e29eb-f729-43e6-9e39-61384524e790','todo',NULL),
    	 ('2cdb87ee-2980-496c-98b4-ec93db93ec90','1d74be1e-cef9-45de-95c7-e6a24234375e','e42e29eb-f729-43e6-9e39-61384524e790','todo',NULL),
    	 ('3e129461-186b-4235-8523-d36986d59970','6ca22435-1dd5-4441-8867-aad03424a322','7583f5f4-ea78-442a-99f4-0caddd45fb19','todo',NULL),
    	 ('c28002b7-a540-4faf-add9-20c150e6feac','6ca22435-1dd5-4441-8867-aad03424a322','e42e29eb-f729-43e6-9e39-61384524e790','todo',NULL),
    	 ('fcf42641-af4b-43ab-9bf2-e7d0ca6fb989','a53ef68c-31aa-4e48-923f-7078de5de2a7','7583f5f4-ea78-442a-99f4-0caddd45fb19','done','2024-09-19 19:00:00+05:30'),
    	 ('a36c0497-faa4-420b-b2c6-99292ed30331','1d74be1e-cef9-45de-95c7-e6a24234375e','7583f5f4-ea78-442a-99f4-0caddd45fb19','done','2024-09-19 19:00:00+05:30');

    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;