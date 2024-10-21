Feature: Task Manager features

  Background:
    Given tasks do not exist

  Scenario: create valid task
    When create task request was called
      | Field       | Value           |
      | title       | SomeTitle       |
      | description | SomeDescription |
      | priority    | 1               |
    Then rest operation should be executed successfully
    And task should be created with next fields
      | Field       | Value           |
      | title       | SomeTitle       |
      | description | SomeDescription |
      | status      | NEW             |
      | priority    | 1               |

  Scenario: create task (invalid title and priority)
    When create task request was called
      | Field       | Value           |
      | title       |                 |
      | description | SomeDescription |
      | priority    | 11              |
    Then parma rest operation should be executed with 400 status
    And response contains the following error(s)
      | message                     |
      | Title can not be empty      |
      | Priority must be at most 10 |

  Scenario: delete task
    Given task *1 exists
      | Field       | Value           |
      | title       | SomeTitle       |
      | description | SomeDescription |
      | priority    | 1               |
    When delete task *1 operation was called
    Then rest operation should be executed successfully
    And task *1 should not exist

  Scenario: update task status
    Given task *2 exists
      | Field       | Value           |
      | title       | SomeTitle       |
      | description | SomeDescription |
      | priority    | 1               |
    When update task status operation for task *2 and status IN_PROGRESS was called
    Then rest operation should be executed successfully
    And task *2 should have next fields
      | Field       | Value           |
      | title       | SomeTitle       |
      | description | SomeDescription |
      | status      | IN_PROGRESS     |
      | priority    | 1               |

  Scenario: update task status when there is no such task
    When update task status operation for taskId 100000 and status IN_PROGRESS was called
    Then parma rest operation should be executed with 400 status
    And response contains the following error(s)
      | code   | message           |
      | TE-001 | Task is not found |

  Scenario: get all tasks
    Given task exists
      | Field       | Value           |
      | title       | TaskOne         |
      | description | SomeDescription |
      | priority    | 1               |
      | status      | IN_PROGRESS     |
    And task exists
      | Field       | Value           |
      | title       | TaskTwo         |
      | description | SomeDescription |
      | priority    | 2               |
      | status      | IN_PROGRESS     |
    When get all tasks operation was called
    Then rest operation should be executed successfully
    And tasks should be returned
      | title   | description     | priority | status      |
      | TaskOne | SomeDescription | 1        | IN_PROGRESS |
      | TaskTwo | SomeDescription | 2        | IN_PROGRESS |

  Scenario: update task title
    Given task *3 exists
      | Field       | Value           |
      | title       | SomeTitle       |
      | description | SomeDescription |
      | priority    | 1               |
      | status      | IN_PROGRESS     |
    When update task status operation for task *3 was called
      | Field       | Value           |
      | title       | NewTitle        |
      | description | SomeDescription |
      | priority    | 1               |
      | status      | IN_PROGRESS     |
    Then rest operation should be executed successfully
    And task *3 should have next fields
      | Field       | Value           |
      | title       | NewTitle        |
      | description | SomeDescription |
      | priority    | 1               |
      | status      | IN_PROGRESS     |

  Scenario: create task when task with the same title has status IN_PROGRESS
    Given task *4 exists
      | Field       | Value           |
      | title       | DuplicateTask   |
      | description | SomeDescription |
      | priority    | 1               |
      | status      | IN_PROGRESS     |
    When create task request was called
      | Field       | Value           |
      | title       | DuplicateTask   |
      | description | SomeDescription |
      | priority    | 1               |
    Then parma rest operation should be executed with 400 status
    And response contains the following error(s)
      | code   | message             | details                             |
      | TE-002 | Task already exists | Task with such title already exists |

  Scenario: create task when task with the same title has status COMPLETED
    Given task *5 exists
      | Field       | Value           |
      | title       | DuplicateTask   |
      | description | SomeDescription |
      | priority    | 1               |
      | status      | COMPLETED       |
    When create task request was called
      | Field       | Value           |
      | title       | DuplicateTask   |
      | description | SomeDescription |
      | priority    | 1               |
    Then rest operation should be executed successfully
    And task should be created with next fields
      | Field       | Value           |
      | title       | DuplicateTask   |
      | description | SomeDescription |
      | priority    | 1               |
      | status      | NEW             |