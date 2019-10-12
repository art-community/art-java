import {
    Avatar,
    Card,
    CardContent,
    CardHeader,
    createStyles,
    IconButton,
    makeStyles,
    Menu,
    MenuItem,
    Theme,
    Typography
} from "@material-ui/core";
import {BuildOutlined, DeleteOutlined, MoreVert} from "@material-ui/icons";
import * as React from "react";
import {useState} from "react";

const useStyles = makeStyles((theme: Theme) => createStyles({
    card: {
        maxWidth: 345
    },
    avatar: {
        backgroundColor: theme.palette.secondary.main,
    },
}));


export const ProjectCardComponent = () => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const styles = useStyles();

    const handleClick = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    return <Card className={styles.card}>
        <CardHeader
            avatar={
                <Avatar className={styles.avatar}>
                    П
                </Avatar>
            }
            action={
                <div>
                    <IconButton color={"primary"} onClick={handleClick}>
                        <MoreVert/>
                    </IconButton>
                    <Menu anchorEl={anchorEl}
                          getContentAnchorEl={null}
                          anchorOrigin={{
                              horizontal: 'right',
                              vertical: 'bottom',
                          }}
                          transformOrigin={{
                              horizontal: 'left',
                              vertical: 'top',
                          }}
                          onClose={handleClose}
                          keepMounted
                          open={open}>
                        <MenuItem>
                            <IconButton color={"primary"} onClick={handleClose}>
                                <BuildOutlined/>
                            </IconButton>
                        </MenuItem>
                        <MenuItem>
                            <IconButton color={"primary"} onClick={handleClose}>
                                <DeleteOutlined/>
                            </IconButton>
                        </MenuItem>
                    </Menu>
                </div>
            }
            title={
                <Typography color={"primary"}>Проект 1</Typography>
            }
        >
        </CardHeader>
        <CardContent>
            <Typography variant="body2" color="textSecondary" component="p">
                This impressive paella is a perfect party dish and a fun meal to cook together with your
                guests. Add 1 cup of frozen peas along with the mussels, if you like.
            </Typography>
        </CardContent>
    </Card>
};