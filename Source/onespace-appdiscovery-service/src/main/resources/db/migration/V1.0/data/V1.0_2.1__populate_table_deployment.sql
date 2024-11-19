BEGIN;
DO $$
BEGIN
    INSERT INTO deployment (deployment_id, deployment_mode)
	VALUES ('150125bc-1aca-4583-b778-0ffad387676b', 'Multi-tenant'),
       ('fea98031-2678-41be-b4f5-70958b5b7c4d', 'Per-Region'),
       ('175f3d4e-579c-4a97-998c-199617494fd2', 'Instance'),
       ('1e2216ca-623f-4787-944a-1bd4923c07d2', 'Not applicable');
    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;        
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;