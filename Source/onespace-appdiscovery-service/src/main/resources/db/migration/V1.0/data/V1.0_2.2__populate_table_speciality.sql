BEGIN;
DO $$
BEGIN
    INSERT INTO speciality (speciality_name)
    VALUES ('Anesthesiology'),
           ('Cardiology'),
           ('Dermatology'),
           ('Emergency Medicine'),
           ('Endocrinology'),
           ('Hematology'),
           ('Nephrology'),
           ('Neurology'),
           ('Obstetrics and Gynecology'),
           ('Oncology'),
           ('Ophthalmology'),
           ('Orthopedics'),
           ('Pediatrics'),
           ('Pulmonology'),
           ('Radiology'),
           ('Surgery'),
           ('Urology'),
           ('Not applicable');
    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;