DO $$
BEGIN
  -- Create generate uuid function
  CREATE OR REPLACE FUNCTION public.gen_random_uuid()
    RETURNS uuid
    LANGUAGE c
    AS '$libdir/pgcrypto', $function$pg_random_uuid$function$
  ;
END
$$;