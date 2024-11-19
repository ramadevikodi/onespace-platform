--App notification function to notify app_notifications channel
CREATE OR REPLACE FUNCTION notify_event_to_channel()
RETURNS TRIGGER AS $$
begin
	PERFORM pg_notify('app_notifications', NEW.id::text);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
