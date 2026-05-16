CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  openid VARCHAR(64) NOT NULL UNIQUE,
  unionid VARCHAR(64) NULL,
  nickname VARCHAR(64) NULL,
  avatar_url VARCHAR(512) NULL,
  status TINYINT NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  last_active_at DATETIME NOT NULL,
  INDEX idx_last_active_at (last_active_at)
);

CREATE TABLE user_profiles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL UNIQUE,
  openid VARCHAR(64) NOT NULL,
  spicy_level INT NOT NULL DEFAULT 2,
  preferred_tastes JSON NULL,
  disliked_tastes JSON NULL,
  favorite_ingredients JSON NULL,
  disliked_ingredients JSON NULL,
  common_ingredients JSON NULL,
  preferred_cooking_time_minutes INT NOT NULL DEFAULT 20,
  preferred_difficulty VARCHAR(16) NOT NULL DEFAULT 'easy',
  cooking_tools JSON NULL,
  health_goal VARCHAR(32) NOT NULL DEFAULT 'none',
  preference_summary VARCHAR(1000) NOT NULL,
  total_generated INT NOT NULL DEFAULT 0,
  total_liked INT NOT NULL DEFAULT 0,
  total_cooked INT NOT NULL DEFAULT 0,
  total_saved INT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  INDEX idx_openid (openid)
);

CREATE TABLE recommendation_sessions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  openid VARCHAR(64) NOT NULL,
  free_text VARCHAR(1000) NOT NULL,
  selected_moods JSON NULL,
  selected_tastes JSON NULL,
  selected_time VARCHAR(32) NULL,
  selected_tools JSON NULL,
  parsed_ingredients JSON NULL,
  parsed_moods JSON NULL,
  parsed_tastes JSON NULL,
  max_time_minutes INT NULL,
  parsed_tools JSON NULL,
  constraints_json JSON NULL,
  profile_snapshot JSON NULL,
  llm_provider VARCHAR(64) NOT NULL,
  llm_model VARCHAR(128) NOT NULL,
  prompt_version VARCHAR(32) NOT NULL,
  status VARCHAR(16) NOT NULL DEFAULT 'success',
  error_message VARCHAR(1000) NULL,
  created_at DATETIME NOT NULL,
  INDEX idx_user_created_at (user_id, created_at),
  INDEX idx_openid_created_at (openid, created_at)
);

CREATE TABLE recommendations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  session_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  openid VARCHAR(64) NOT NULL,
  type VARCHAR(32) NOT NULL,
  name VARCHAR(128) NOT NULL,
  reason VARCHAR(255) NOT NULL,
  estimated_time_minutes INT NOT NULL,
  difficulty VARCHAR(16) NOT NULL,
  ingredients_used JSON NULL,
  missing_ingredients JSON NULL,
  steps JSON NOT NULL,
  substitutions JSON NULL,
  tags JSON NULL,
  caution VARCHAR(255) NULL,
  liked TINYINT NOT NULL DEFAULT 0,
  disliked TINYINT NOT NULL DEFAULT 0,
  saved TINYINT NOT NULL DEFAULT 0,
  cooked TINYINT NOT NULL DEFAULT 0,
  too_hard TINYINT NOT NULL DEFAULT 0,
  too_light TINYINT NOT NULL DEFAULT 0,
  too_oily TINYINT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  INDEX idx_session_id (session_id),
  INDEX idx_user_created_at (user_id, created_at)
);

CREATE TABLE feedback_events (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  openid VARCHAR(64) NOT NULL,
  session_id BIGINT NOT NULL,
  recommendation_id BIGINT NOT NULL,
  action VARCHAR(32) NOT NULL,
  dish_name VARCHAR(128) NOT NULL,
  dish_tags JSON NULL,
  dish_ingredients JSON NULL,
  created_at DATETIME NOT NULL,
  INDEX idx_user_created_at (user_id, created_at),
  INDEX idx_recommendation_id (recommendation_id)
);

CREATE TABLE favorites (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  openid VARCHAR(64) NOT NULL,
  recommendation_id BIGINT NOT NULL,
  name VARCHAR(128) NOT NULL,
  tags JSON NULL,
  estimated_time_minutes INT NOT NULL,
  ingredients_used JSON NULL,
  steps JSON NULL,
  created_at DATETIME NOT NULL,
  UNIQUE KEY uk_user_recommendation (user_id, recommendation_id),
  INDEX idx_user_created_at (user_id, created_at)
);

CREATE TABLE analytics_events (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NULL,
  openid VARCHAR(64) NULL,
  event_name VARCHAR(64) NOT NULL,
  properties JSON NULL,
  created_at DATETIME NOT NULL,
  INDEX idx_event_created_at (event_name, created_at),
  INDEX idx_user_created_at (user_id, created_at)
);
