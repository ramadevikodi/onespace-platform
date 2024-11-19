BEGIN;
DO $$
BEGIN

    INSERT INTO business_unit(business_unit_id, cluster, segment, business_unit_name, description)
    VALUES ('6c349e5b-c950-47e0-bb99-01b1b8922ad1', 'Innovation  & Strategy', 'HealthSuite Services and Analytics Platform',
            'EPO', 'Enterprise Platform Orchestrator'),
           ('a5808bc0-91cc-40d3-b5f4-159075bef93b', 'Diagnosis and Treatment', 'Precision Diagnosis', 'Ultrasound',
            'Ultrasound business'),
           ('a5808bc0-91cc-40d3-b5f4-169075bef93b', 'Diagnosis and Treatment', 'Precision Diagnosis', 'DXR',
            'Ultrasound business'),
           ('a5808bc0-91cc-40d3-b5f4-179075bef93b', 'Diagnosis and Treatment', 'Precision Diagnosis', 'MRI',
            'Ultrasound business'),
           ('a5808bc0-91cc-40d3-b5f4-189075bef93b', 'Diagnosis and Treatment', 'Precision Diagnosis', 'CT',
            'Ultrasound business'),
           ('a5808bc0-91cc-40d3-b5f4-199075bef93b', 'Diagnosis and Treatment', 'Image Guided Therapy', 'Systems',
            'Ultrasound business'),
           ('a5808bc0-91cc-40d3-b5f4-209075bef93b', 'Diagnosis and Treatment', 'Image Guided Therapy', 'Devices',
            'Ultrasound business'),
           ('a5808bc0-91cc-40d3-b5f4-219075bef93b', 'Connected Care', 'Monitoring', 'Hospital Patient Monitoring (HPM)',
            'Hospital Patient Monitoring'),
           ('a5808bc0-91cc-40d3-b5f4-229075bef93b', 'Connected Care', 'Monitoring',
            'Ambulatory Monitoring and Diagnostics (AM&D)',
            'Ambulatory Monitoring and Diagnostics'),
           ('a5808bc0-91cc-40d3-b5f4-239075bef93b', 'Connected Care', 'Monitoring', 'Emergency Care',
            'Emergency Care'),
           ('a5808bc0-91cc-40d3-b5f4-249075bef93b', 'Connected Care', 'Enterprise Informatics', 'EMR & CM',
            'EMR & CM'),
           ('a5808bc0-91cc-40d3-b5f4-259075bef93b', 'Connected Care', 'Enterprise Informatics',
            'Cardiovascular Informatics', 'Cardiovascular Informatics'),
           ('a5808bc0-91cc-40d3-b5f4-269075bef93b', 'Connected Care', 'Enterprise Informatics',
            'Clinical Integration and Insights', 'Clinical Integration and Insights'),
           ('a5808bc0-91cc-40d3-b5f4-279075bef93b', 'Connected Care', 'Enterprise Informatics',
            'Clinical Insights and Informatics (CDS)', 'Clinical Insights and Informatics (CDS)'),
           ('a5808bc0-91cc-40d3-b5f4-289075bef93b', 'Personal Health', 'Personal Health',
            'Oral Healthcare', 'Oral Healthcare'),
           ('a5808bc0-91cc-40d3-b5f4-299075bef93b', 'Personal Health', 'Personal Health',
            'Mother and Child', 'Mother and Child'),
           ('a5808bc0-91cc-40d3-b5f4-309075bef93b', 'Personal Health', 'Personal Health',
            'Personal Care', 'Personal Care');


     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;