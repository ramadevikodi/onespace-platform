BEGIN;
DO $$
BEGIN
    
    -- INSERT DATA CODE
     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN        
    ROLLBACK;        
    RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;