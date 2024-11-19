BEGIN;
DO $$
BEGIN
    ALTER TABLE action_potential_owners RENAME TO action_owners;
    ALTER TABLE action_owners ADD COLUMN action_owner_id UUID DEFAULT gen_random_uuid() PRIMARY KEY;
    ALTER TABLE action_owners ADD COLUMN status VARCHAR(20);
    ALTER TABLE action_owners ADD COLUMN completed_at_datetime TIMESTAMP WITH TIME ZONE;

    RAISE NOTICE 'Added columns to table "action_owners" successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the table alteration. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;