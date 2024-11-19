BEGIN;
DO $$
BEGIN
    INSERT INTO application_status (status_name)
    values ('Draft'),
           ('Submit for Approval'),
           ('Awaiting Business Owner Approval'),
           ('Approved by Business Owner'),
           ('Rejected by Business Owner'),
           ('On Hold by Business Owner'),
           ('Awaiting Market/Solution Owner Approval'),
           ('Approved by Market/Solution Owner'),
           ('On Hold by Market/Solution Owner'),
           ('Awaiting Requester action'),
           ('Withdrawn'),
           ('Approved'),
           ('Canceled'),
           ('Published');

    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;