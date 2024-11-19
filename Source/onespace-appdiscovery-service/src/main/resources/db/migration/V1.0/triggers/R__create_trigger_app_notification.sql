--App notification trigger to notify app_notifications channel
CREATE OR REPLACE TRIGGER appnotification_trigger
AFTER INSERT OR UPDATE ON app_notification
FOR EACH ROW
EXECUTE FUNCTION notify_event_to_channel();